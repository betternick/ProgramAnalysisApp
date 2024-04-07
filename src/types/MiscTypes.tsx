import { HandleType, Position } from 'reactflow'

export type HandleInfo = {
    position: Position
    type: HandleType
    id: string
}

export type CustomNodeProps = {
    color: string
    label: string
    handles: HandleInfo[]
    comments: string[]
    code?: string
}

export type CustomNodeCollapsibleProps = {
    color: string
    label: string
    handles: HandleInfo[]
    comments: string[]
    children: string[]
}

export type CustomNodeWithCodeProps = {
    color: string
    label: string
    handles: HandleInfo[]
    comments: string[]
    code: string
    dynamicData: DynamicData | undefined
}

export type DynamicData = {
    executionTimes: number
    averageExecutionTime: number
    averageMemoryUsage: number
    averageCpuUsage: number
    scaleFactor: number
}

export type DynamicDataCalc = {
    id: string
    val: number
    deviation: number
    scaleFactor: number
}
