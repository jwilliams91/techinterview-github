import { SimpleEvent } from './SimpleEvent';

export class EventDetails extends SimpleEvent{
	orginization: string;
    members: string[];
    
    public constructor () {
        super();
    }
}