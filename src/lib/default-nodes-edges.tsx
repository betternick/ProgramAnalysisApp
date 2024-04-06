import { Edge, Node, MarkerType } from 'reactflow'
import { NodeType, entryNode, exitNode, statementNode } from './nodeTypes'

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
    { id: '1', position: { x: 0, y: 0 }, data: entryNode, type: NodeType.BASIC_NODE },
    {
        id: '2',
        position: { x: 0, y: 0 },
        data: { code: 'System.out.println("Hello World!")', comments: [], ...statementNode },
        type: NodeType.STATEMENT_NODE,
    },
    { id: '3', position: { x: 0, y: 0 }, data: exitNode, type: NodeType.BASIC_NODE },
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
