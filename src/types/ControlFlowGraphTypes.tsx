export interface ResponseGraph {
    name: string
    nodes: ResponseNode[]
    edges: ResponseEdge[]
}

export type ResponseNode = {
    codeBlock: ResponseCodeBlock
    id: string
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
