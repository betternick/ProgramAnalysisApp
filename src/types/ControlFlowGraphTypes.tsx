import { DynamicDataDeviationCalc, DynamicDataDeviationPair } from './MiscTypes'

export interface ResponseGraph {
    name: string
    nodes: ResponseNode[]
    edges: ResponseEdge[]
}

export type ResponseNode = {
    codeBlock: ResponseCodeBlock
    id: string
    comments: string[]
    dynamicData?: ResponseDynamicData
}

export type ResponseEdge = {
    id: string
    source: string
    target: string
}

export type ResponseCodeBlock = {
    code: string[]
    fileName: string
    lineStart: number
}

export type ResponseDynamicData = {
    executionTimes: number
    averageExecutionTime: number
    averageMemoryUsage: number
    averageCpuUsage: number
    deviations: DynamicDataDeviationCalc
    stat?: string
}
