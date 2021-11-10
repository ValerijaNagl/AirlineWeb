import { Airline } from './airline.model';
import { Flight } from './flight.model';

export interface Ticket {
          id:Number,
          airline: Airline,
          oneway: Boolean,
          departDate: Date,
          returnDate: Date,
          count: Number,
          flight: Flight
}