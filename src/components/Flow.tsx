import React, { useState, useEffect, useCallback } from 'react'
import ReactFlow, {
    Edge,
    useReactFlow,
    useNodesInitialized,
    applyNodeChanges,
    MiniMap,
    Controls,
    Node,
    Position,
    applyEdgeChanges,
    NodeChange,
    EdgeChange,
} from 'reactflow'
import Dagre from '@dagrejs/dagre'
import { nodeTypes } from '../lib/nodeTypes'

const getLayoutedElements = (nodes: Node[], edges: Edge[], intersectingNodes: number) => {
    const dagreGraph = new Dagre.graphlib.Graph()
    dagreGraph.setDefaultEdgeLabel(() => ({}))

    dagreGraph.setGraph({ rankdir: 'TB' })
    nodes.forEach((node) => {
        dagreGraph.setNode(node.id, { width: node.width ? node.width : 200, height: node.height ? node.height : 100 })
    })
    edges.forEach((edge) => {
        dagreGraph.setEdge(edge.source, edge.target)
    })
    Dagre.layout(dagreGraph)
    const hiddenNodes: string[] = []
    nodes.forEach((node) => {
        node.targetPosition = Position.Top
        node.sourcePosition = Position.Bottom
        const nodeWithPosition = dagreGraph.node(node.id)
        if (node.parentNode) {
            const parentNodeWithPosition = dagreGraph.node(node.parentNode)
            node.position = {
                x: nodeWithPosition.x - (parentNodeWithPosition.x - parentNodeWithPosition.width / 2) - (node.width ? node.width : 300) / 2,
                y: nodeWithPosition.y - (parentNodeWithPosition.y - parentNodeWithPosition.height / 2) - (node.height ? node.height : 200) / 2,
            }
            node.hidden = !node.hidden
        } else {
            node.position = {
                x: nodeWithPosition.x - nodeWithPosition.width / 2,
                y: nodeWithPosition.y - nodeWithPosition.height / 2,
            }
        }
        if (node.hidden) hiddenNodes.push(node.id)
        node.style = { opacity: 1 }
        return node
    })
    edges.forEach((edge) => {
        if (hiddenNodes.includes(edge.source) || hiddenNodes.includes(edge.target)) {
            edge.hidden = true
        } else {
            edge.hidden = false
        }
        edge.style = { opacity: 1 }
    })
    return { nodes, edges }
}

type FlowProps = {
    nodes: Node[]
    edges: Edge[]
}

export default function Flow({ nodes, edges }: FlowProps) {
    const [layoutedNodes, setLayoutedNodes] = useState<Node[]>([])
    const [layoutedEdges, setLayoutedEdges] = useState<Edge[]>([])

    const { getNodes, getEdges, getIntersectingNodes, fitView } = useReactFlow()
    const nodesInitialized = useNodesInitialized({
        includeHiddenNodes: true,
    })

    useEffect(() => {
        nodes.forEach((n) => {
            n.style = { opacity: 0 }
        })
        edges.forEach((e) => {
            e.style = { opacity: 0 }
        })
        setLayoutedNodes(nodes)
        setLayoutedEdges(edges)
    }, [nodes, edges])

    const intersectingNodes = getNodes()
        .map((n) => getIntersectingNodes(n).length)
        .reduce((accum, num) => (accum += num), 0)

    useEffect(() => {
        const numNodes = getNodes().length
        if (nodesInitialized && intersectingNodes >= numNodes / 2) {
            const { nodes: newLayoutedNodes, edges: newLayoutedEdges } = getLayoutedElements(getNodes(), getEdges(), intersectingNodes)
            setLayoutedNodes(newLayoutedNodes)
            setLayoutedEdges(newLayoutedEdges)
        }
    }, [getEdges, getNodes, nodesInitialized, intersectingNodes])

    const handleNodeClick = (e, parent: Node) => {
        if (parent.data.children) {
            setLayoutedNodes((prevNodes) =>
                prevNodes.map((n) => {
                    if (parent.data.children.includes(n.id)) {
                        n.hidden = !n.hidden
                    }
                    return n
                }),
            )
            setLayoutedEdges((prevEdges) =>
                prevEdges.map((e) => {
                    if (parent.data.children.includes(e.source) || parent.data.children.includes(e.target)) {
                        return { ...e, hidden: !e.hidden }
                    }
                    return e
                }),
            )
        }
    }

    const onNodesChange = useCallback((changes: NodeChange[]) => setLayoutedNodes((nds) => applyNodeChanges(changes, nds)), [])
    const onEdgesChange = useCallback((changes: EdgeChange[]) => setLayoutedEdges((eds) => applyEdgeChanges(changes, eds)), [setLayoutedEdges])

    return (
        <ReactFlow
            nodes={layoutedNodes}
            edges={layoutedEdges}
            onNodesChange={onNodesChange}
            onEdgesChange={onEdgesChange}
            onNodeClick={handleNodeClick}
            nodeTypes={nodeTypes}
            fitView
        >
            <MiniMap
                zoomable
                pannable
            />
            <Controls />
        </ReactFlow>
    )
}
