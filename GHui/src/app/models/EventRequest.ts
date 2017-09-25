export class EventRequest {
    eventId: number;
    username: string;

    public constructor(eventId:number, username: string) {
        this.eventId = eventId;
        this.username = username;
    }
}