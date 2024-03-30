import { Flex } from '@chakra-ui/react'
import React, { useState, useEffect, useCallback } from 'react'
import ReactFlow, { applyNodeChanges, Node, Edge, Controls, ReactFlowProvider, useNodes, useEdges } from 'reactflow'
import { defaultNodes, defaultEdges } from '../lib/default-nodes-edges'
import 'reactflow/dist/style.css'
import { ResponseEdge, ResponseGraph, ResponseNode } from '../types/ControlFlowGraphTypes'
import Dagre from '@dagrejs/dagre'

type ControlFlowGraphProps = {
    graph: ResponseGraph[]
}

const dagreGraph = new Dagre.graphlib.Graph()
dagreGraph.setDefaultEdgeLabel(() => ({}))
const nodeWidth = 200
const nodeHeight = 100

const getLayoutedElements = (nodes, edges, direction = 'TB') => {
    const isHorizontal = direction === 'LR'
    dagreGraph.setGraph({ rankdir: direction })

    nodes.forEach((node) => {
        dagreGraph.setNode(node.id, { width: nodeWidth, height: nodeHeight })
    })
    edges.forEach((edge) => {
        dagreGraph.setEdge(edge.source, edge.target)
    })
    Dagre.layout(dagreGraph)

    nodes.forEach((node) => {
        const nodeWithPosition = dagreGraph.node(node.id)
        node.targetPosition = isHorizontal ? 'left' : 'top'
        node.sourcePosition = isHorizontal ? 'right' : 'bottom'
        node.position = {
            x: nodeWithPosition.x - nodeWidth / 2,
            y: nodeWithPosition.y - nodeHeight / 2,
        }

        return node
    })

    return { nodes, edges }
}

const { nodes: initialNodes, edges: initialEdges } = getLayoutedElements(defaultNodes, defaultEdges)

const createGraphLayout = (graph, flowNodeStates: Node[], edges): Node[] => {
    flowNodeStates.forEach((node) => {
        graph.setNode(node.id, {
            label: node.id,
            width: node.width, // TODO: probably wrong
            height: node.height, // TODO: probably wrong
        })
    })
    edges.forEach((edge) => {
        graph.setEdge(edge.source, edge.target)
    })
    Dagre.layout(graph)
    return flowNodeStates.map((nodeState) => {
        const node = graph.node(nodeState.id)
        return {
            ...nodeState,
            position: {
                x: node.x - node.width / 2 + Math.random() / 1000,
                y: node.y - node.height / 2,
            },
        }
    })
}

const NodeLayouter = (props) => {
    const nodeStates = useNodes()
    const edgeStates = useEdges()

    const nodeHasDimension = (node) => node.__rf != null && node.position != null

    const changeLayout = () => {
        if (nodeStates.length > 0 && nodeStates.every(nodeHasDimension)) {
            const nodesWithLayout = createGraphLayout(props.graph, nodeStates, edgeStates)
            props.nodesSetter(nodesWithLayout)
        }
        return (
            <button
                onClick={() => {
                    changeLayout()
                }}
            >
                change layout
            </button>
        )
    }
    return (
        <button
            className="tightLayouterButton"
            onClick={() => {
                changeLayout()
            }}
        >
            change layout
        </button>
    )
}

export default function ControlFlowGraph({ graph }: ControlFlowGraphProps) {
    const [nodes, setNodes] = useState<Node[]>(initialNodes)
    const [edges, setEdges] = useState<Edge[]>(initialEdges)

    const processResponseNodes = useCallback((cfg: ResponseGraph, i: number): Node[] => {
        const x = i * 200
        const entryNode = cfg.nodes.find((node) => node.codeBlock.code[0] === 'Entry')
        const exitNode = cfg.nodes.find((node) => node.codeBlock.code[0] === 'Exit')

        const nodeList: Node[] = []

        entryNode && nodeList.push(createNode(entryNode, x, 0))

        cfg.nodes.forEach((n, j) => {
            if (n.id !== entryNode?.id && n.id !== exitNode?.id) {
                nodeList.push(createNode(n, x, (j + 1) * 100))
            }
        })
        exitNode && nodeList.push(createNode(exitNode, x, (nodeList.length + 1) * 100))
        return nodeList
    }, [])

    const processResponseEdges = useCallback((cfg: ResponseGraph): Edge[] => {
        return cfg.edges.map((e) => createEdge(e))
    }, [])

    const createNode = (responseNode: ResponseNode, x: number, y: number): Node => {
        return {
            id: responseNode.id,
            position: { x, y },
            data: { label: responseNode.codeBlock.code },
        }
    }

    const createEdge = (responseEdge: ResponseEdge): Edge => {
        return {
            ...responseEdge,
        }
    }

    useEffect(() => {
        if (graph.length === 0) return
        let newNodeList: Node[] = []
        let newEdgeList: Edge[] = []
        graph.forEach((cfg, i) => {
            if (i === 0) {
                // TODO: remove
                const x = i * 200
                newNodeList = [...newNodeList, ...processResponseNodes(cfg, i)]
                newEdgeList = [...newEdgeList, ...processResponseEdges(cfg)]
            }
        })
        setNodes(newNodeList)
        setEdges(newEdgeList)
    }, [graph, processResponseEdges, processResponseNodes])

    const onNodesChange = useCallback((changes) => setNodes((nds) => applyNodeChanges(changes, nds)), [])

    return (
        <Flex
            height="100vh"
            width="100vw"
        >
            <ReactFlowProvider>
                <ReactFlow
                    nodes={nodes}
                    edges={edges}
                    onNodesChange={onNodesChange}
                    fitView
                />
                <Controls />
                <NodeLayouter
                    graph={dagreGraph}
                    nodesSetter={setNodes}
                />
            </ReactFlowProvider>
        </Flex>
    )
}
