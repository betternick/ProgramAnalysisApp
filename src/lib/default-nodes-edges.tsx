import { MarkerType } from 'reactflow'

export const defaultNodes = [
    { id: '1', position: { x: 0, y: 100 }, data: { label: 'Entry' } },
    { id: '2', position: { x: 0, y: 200 }, data: { label: 'System.out.println("Hello World!")' } },
    { id: '3', position: { x: 0, y: 300 }, data: { label: 'Exit' } },
]
export const defaultEdges = [
    {
        id: 'e1-2',
        source: '1',
        target: '2',
        type: 'straight',
        markerEnd: {
            type: MarkerType.Arrow,
        },
    },
    {
        id: 'e2-3',
        source: '2',
        target: '3',
        type: 'straight',
        markerEnd: {
            type: MarkerType.Arrow,
        },
    },
]
