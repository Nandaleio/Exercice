import { Rule } from "./rule-model";

export interface Game {
    id: number,
    name: string,
    rule: Rule,
    players?: Player[],
    frames: Frame[];
}

export interface Frame {
    id: number;
    frameNumber: number;
    rolls: number[];
    totalScore: number;
    player: Player;
}

export interface Player {
    id:number; 
    username:string
};