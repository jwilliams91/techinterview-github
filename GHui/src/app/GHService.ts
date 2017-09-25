import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { Constants } from './Constants';
import { SimpleEvent } from './models/SimpleEvent';
import { User } from './models/User';
import { EventRequest } from './models/EventRequest';
import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';


@Injectable()
export class GHService {

    public logonSuccess$: EventEmitter<String>;
    eventData: SimpleEvent[];

    constructor(private http: Http) {
        this.logonSuccess$ = new EventEmitter();
    }

    getUserAvatar(username: string) {
        var url = Constants.API_URL + `/users/getUserAvatar`;
        url = this.appendToken(url) + `&username=${username}`;
        return this.http.get(url).catch(this.catchError).toPromise()
                        .then((res) => res._body as string);
    }

    getUserAvatarsBulk(users: string[]) {
        var url = Constants.API_URL + '/users/getUserAvatarBulk';
        url = this.appendToken(url);
        return this.http.post(url, users).catch(this.catchError).toPromise()
                        .then((res) => res.json() as string[]);
    }

    getCurrentUser() {
        var url = Constants.API_URL + '/users/current';
        url = this.appendToken(url);
        return this.http.get(url)
            .catch(this.catchError).toPromise();
    }

    authorizeUser(code: string) {
        var url = Constants.API_URL + '/users/authenticate';
        return this.http.post(url, code)
            .catch(this.catchError).toPromise()
            .then((res) => {
                sessionStorage.setItem("currentUser", res._body);
                this.logonSuccess$.emit("Success");
            });
    }

    getEvents(eventListType: string): Promise<SimpleEvent[]> {
        var url = Constants.API_URL;
        url += eventListType == 'public' ? '/events/list' : '/events/listByCurrentUser';
        url = this.appendToken(url);
        return this.http.get(url)
                        .catch(this.catchError).toPromise()
                        .then((res) => this.eventData = res.json() as SimpleEvent[]);
    }

    getEventDetails(eventRequest: EventRequest): Promise<SimpleEvent> {
        var url = Constants.API_URL + `/events/getEventDetails`;
        url = this.appendToken(url);
        return this.http.post(url, eventRequest)
                        .catch(this.catchError).toPromise()
                        .then((res) => res.json() as SimpleEvent);
    }

    private catchError(error: any): Promise<any> {
        console.error('An error occured', error);
        return Promise.reject(error.message || error);
    }

    private appendToken(url: string) {
        var current = JSON.parse(sessionStorage.getItem("currentUser")) as User;
        return url += `?token=${current.token}`;
    }
}