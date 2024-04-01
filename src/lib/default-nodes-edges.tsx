import { Edge, Node, MarkerType } from 'reactflow'

export const edgeFormat = {
    markerEnd: {
        type: MarkerType.ArrowClosed,
        width: 20,
        height: 20,
        color: '#808080',
    },
    style: {
        strokeWidth: 1,
        stroke: '#808080',
    },
    type: 'smoothstep',
}

export const defaultNodes: Node[] = [
    { id: '1', position: { x: 0, y: 0 }, data: { label: 'Entry' }, type: 'entryNode' },
    { id: '2', position: { x: 0, y: 0 }, data: { label: 'Statement: System.out.println("Hello World!")' }, type: 'statementNode' },
    { id: '3', position: { x: 0, y: 0 }, data: { label: 'Exit' }, type: 'exitNode' },
]
export const defaultEdges: Edge[] = [
    {
        id: 'e1-2',
        source: '1',
        target: '2',
        ...edgeFormat,
    },
    {
        id: 'e2-3',
        source: '2',
        target: '3',
        ...edgeFormat,
    },
]
