import {Booking} from "./booking.model"

export interface User {
    password:String,
    type:String,
    username: string,
    bookings: Booking[]
}