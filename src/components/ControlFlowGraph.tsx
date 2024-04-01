import { Flex } from '@chakra-ui/react'
import React from 'react'
import { Node, Edge, ReactFlowProvider } from 'reactflow'
import { defaultNodes, defaultEdges, edgeFormat } from '../lib/default-nodes-edges'
import 'reactflow/dist/style.css'
import { ResponseEdge, ResponseGraph, ResponseNode } from '../types/ControlFlowGraphTypes'
import Flow from './Flow'
import {
    AFTER_IF_ELSE,
    AFTER_LOOP,
    ENTRY,
    EXIT,
    FALSE_BRANCH,
    FOR_LOOP,
    IF_CONDITION,
    IF_ELSE,
    LOOP_BODY,
    LOOP_CONDITION,
    NodeType,
    STATEMENT,
    TRUE_BRANCH,
    WHILE_LOOP,
} from '../lib/nodeTypes'

const processResponseNodes = (cfg: ResponseGraph): Node[] => {
    return cfg.nodes.map((n) => createNode(n))
}

const processResponseEdges = (cfg: ResponseGraph): Edge[] => {
    return cfg.edges.map((e) => createEdge(e))
}

const createNode = (responseNode: ResponseNode): Node => {
    return {
        id: responseNode.id,
        position: { x: 0, y: 0 }, // dagre will determine pos
        data: { label: responseNode.codeBlock.code[0] },
        type: determineNodeType(responseNode.codeBlock.code[0]),
    }
}

const determineEdgeHandle = (edge: Edge, nodeList: Node[]) => {
    const source = nodeList.find((n) => n.id === edge.source)
    const target = nodeList.find((n) => n.id === edge.target)
    if (source?.type === NodeType.LOOP_BODY_NODE && target?.type === NodeType.LOOP_CONDITION_NODE) {
        edge.sourceHandle = 'c'
        edge.targetHandle = 'c'
    } else if (source?.type === NodeType.LOOP_CONDITION_NODE && target?.type === NodeType.LOOP_BODY_NODE) {
        edge.sourceHandle = 'b'
        edge.targetHandle = 'a'
    } else if (source?.type === NodeType.LOOP_CONDITION_NODE && target?.type === NodeType.AFTER_LOOP_NODE) {
        edge.sourceHandle = 'b'
        edge.targetHandle = 'a'
    }
}

const determineNodeType = (code: string): string | undefined => {
    if (code === ENTRY) {
        return NodeType.ENTRY_NODE
    } else if (code === EXIT) {
        return NodeType.EXIT_NODE
    } else if (code.startsWith(STATEMENT)) {
        return NodeType.STATEMENT_NODE
    } else if (code.startsWith(IF_ELSE)) {
        return NodeType.IF_ELSE_NODE
    } else if (code.startsWith(IF_CONDITION)) {
        return NodeType.IF_CONDITION_NODE
    } else if (code === TRUE_BRANCH) {
        return NodeType.TRUE_BRANCH_NODE
    } else if (code === FALSE_BRANCH) {
        return NodeType.FALSE_BRANCH_NODE
    } else if (code === AFTER_IF_ELSE) {
        return NodeType.AFTER_IF_ELSE_NODE
    } else if (code.startsWith(FOR_LOOP) || code.startsWith(WHILE_LOOP)) {
        return NodeType.LOOP_NODE
    } else if (code.startsWith(LOOP_CONDITION)) {
        return NodeType.LOOP_CONDITION_NODE
    } else if (code === LOOP_BODY) {
        return NodeType.LOOP_BODY_NODE
    } else if (code === AFTER_LOOP) {
        return NodeType.AFTER_LOOP_NODE
    } else {
        return undefined
    }
}

const createEdge = (responseEdge: ResponseEdge): Edge => {
    return {
        ...responseEdge,
        ...edgeFormat,
    }
}

type ControlFlowGraphProps = {
    graph: ResponseGraph[]
}

export default function ControlFlowGraph({ graph }: ControlFlowGraphProps) {
    const controlFlowGraph = (() => {
        if (!graph || graph.length === 0) {
            return (
                <Flow
                    nodes={defaultNodes}
                    edges={defaultEdges}
                />
            )
        } else {
            let newNodeList: Node[] = []
            let newEdgeList: Edge[] = []
            graph.forEach((cfg) => {
                newNodeList = [...newNodeList, ...processResponseNodes(cfg)]
                newEdgeList = [...newEdgeList, ...processResponseEdges(cfg)]
            })
            newEdgeList.forEach((edge) => {
                determineEdgeHandle(edge, newNodeList)
            })
            return (
                <Flow
                    nodes={newNodeList}
                    edges={newEdgeList}
                />
            )
        }
    })()
    return (
        <Flex
            height="100vh"
            width="100vw"
        >
            <ReactFlowProvider>{controlFlowGraph}</ReactFlowProvider>
        </Flex>
    )
}
