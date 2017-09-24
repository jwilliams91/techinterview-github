import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { Constants } from './Constants';
import { SimpleEvent } from './models/SimpleEvent';
import { EventDetails } from './models/EventDetails';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';


@Injectable()
export class GHService {

    public logonSuccess$: EventEmitter<String>;

    constructor(private http: Http) {
        this.logonSuccess$ = new EventEmitter();
    }

    getUser(username: string): any {
        var url = Constants.API_URL + `/users/get?username=${username}`;
        return this.http.get(url)
            .toPromise();
    }

    getUserAvatar(username: string) {
        var url = Constants.API_URL + `/users/getUserAvatar?username=${username}`;
        return this.http.get(url).catch(this.catchError).toPromise()
                        .then((res) => res._body as string);
    }

    getUserAvatarsBulk(users: string[]) {
        var url = Constants.API_URL + '/users/getUserAvatarBulk';
        return this.http.post(url, users).catch(this.catchError).toPromise()
                        .then((res) => res.json() as string[]);
    }

    getCurrentUser() {
        var url = Constants.API_URL + '/users/current';
        return this.http.get(url)
            .catch(this.catchError).toPromise();
    }

    authorizeUser(code: string) {
        var url = Constants.API_URL + '/users/authenticate';
        return this.http.post(url, code)
            .catch(this.catchError).toPromise()
            .then((user) => {
                sessionStorage.setItem("currentUser", user._body);
                this.logonSuccess$.emit("Success");
            });
    }

    getEvents(): Promise<SimpleEvent[]> {
        var url = Constants.API_URL + '/events/list';
        return this.http.get(url)
                        .catch(this.catchError).toPromise()
                        .then((res) => res.json() as SimpleEvent[]);
    }

    getEventsByCurrent(): Promise<SimpleEvent[]> {
        var url = Constants.API_URL + '/events/listByCurrentUser';
        return this.http.get(url)
                        .catch(this.catchError).toPromise()
                        .then((res) => res.json() as SimpleEvent[]);
    }

    getEventDetails(username: string, eventId: number): Promise<SimpleEvent> {
        var url = Constants.API_URL + `/events/getEventDetails?username=${username}&eventId=${eventId}`;
        return this.http.get(url)
                        .catch(this.catchError).toPromise()
                        .then((res) => res.json() as SimpleEvent);
    }

    private catchError(error: any): Promise<any> {
        console.error('An error occured', error);
        return Promise.reject(error.message || error);
    }
}