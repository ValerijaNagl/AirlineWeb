import { City } from './city.model';
import { Ticket } from './ticket.model';

export interface Flight {
    id:Number,
    tickets: Ticket[],
    origin: City,
    destination: City
}