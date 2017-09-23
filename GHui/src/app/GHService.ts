import { Injectable, EventEmitter } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { Constants } from './Constants';
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
        var url = Constants.API_URL + '/users/get?username=' + username;
        return this.http.get(url).map(this.extractData)
            .toPromise();
    }

    getCurrentUser() {
        var url = Constants.API_URL + '/users/current';
        return this.http.get(url).map(this.extractData)
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

    private extractData(res) {
        return res.json();
    }

    private catchError(error: any): Promise<any> {
        console.error('An error occured', error);
        return Promise.reject(error.message || error);
    }
}