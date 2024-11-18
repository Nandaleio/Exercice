import { Rule } from "./rule-model";

export interface Game {
    id: number,
    name: string,
    rule: Rule,
    players: string[],
    frames: any[]

}