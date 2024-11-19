import { Rule } from "./rule-model";
import { Scoreboard } from "./scoreboard-model";

export interface Game {
    id: number,
    name: string,
    rule: Rule,
    players?: {id:number, name:string}[],
    frames: Scoreboard;

}