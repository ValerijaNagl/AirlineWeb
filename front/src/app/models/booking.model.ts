import { Ticket } from './ticket.model';
import { Flight } from './flight.model';
import { User } from './user.model';

export interface Booking {
    id:Number,
    flight: Flight,
    ticket: Ticket,
    available: Boolean
}