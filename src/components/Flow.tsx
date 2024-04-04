import React, { useState, useEffect, useCallback } from 'react'
import ReactFlow, { Edge, useReactFlow, useNodesInitialized, applyNodeChanges, MiniMap, Controls, Node, Position } from 'reactflow'
import Dagre from '@dagrejs/dagre'
import { nodeTypes } from '../lib/nodeTypes'

const getLayoutedElements = (nodes: Node[], edges: Edge[]) => {
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
    nodes.forEach((node) => {
        const nodeWithPosition = dagreGraph.node(node.id)
        node.targetPosition = Position.Top
        node.sourcePosition = Position.Bottom
        node.position = {
            x: nodeWithPosition.x - (node.width ? node.width : 200) / 2,
            y: nodeWithPosition.y - (node.height ? node.height : 100) / 2,
        }
        node.style = { opacity: 100 }
        return node
    })
    edges.forEach((edge) => {
        edge.style = { opacity: 100 }
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
    const reactFlow = useReactFlow()
    const nodesInitialized = useNodesInitialized({
        includeHiddenNodes: false,
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

    useEffect(() => {
        if (nodesInitialized) {
            const { nodes: newLayoutedNodes, edges: newLayoutedEdges } = getLayoutedElements(reactFlow.getNodes(), reactFlow.getEdges())
            setLayoutedNodes(newLayoutedNodes)
            setLayoutedEdges(newLayoutedEdges)
        }
    }, [nodesInitialized, reactFlow])

    useEffect(() => {
        reactFlow.fitView()
    }, [layoutedNodes, layoutedEdges, reactFlow])

    const onNodesChange = useCallback((changes) => setLayoutedNodes((nds) => applyNodeChanges(changes, nds)), [])
    return (
        <ReactFlow
            nodes={layoutedNodes}
            edges={layoutedEdges}
            onNodesChange={onNodesChange}
            nodeTypes={nodeTypes}
        >
            <MiniMap
                zoomable
                pannable
            />
            <Controls />
        </ReactFlow>
    )
}
