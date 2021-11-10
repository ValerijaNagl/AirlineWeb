import { User } from './user.model';

export class Group {
    constructor( public users: User[],
                 public groupName: string) {}
 
}