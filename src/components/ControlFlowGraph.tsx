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
    afterIfElseNode,
    afterLoopNode,
    entryNode,
    exitNode,
    falseBranchNode,
    ifConditionNode,
    ifElseNode,
    loopBodyNode,
    loopConditionNode,
    loopNode,
    statementNode,
    trueBranchNode,
} from '../lib/nodeTypes'

const processResponseNodes = (cfg: ResponseGraph): Node[] => {
    return cfg.nodes.map((n) => createNode(n))
}

const processResponseEdges = (cfg: ResponseGraph): Edge[] => {
    return cfg.edges.map((e) => createEdge(e))
}

const createNode = (responseNode: ResponseNode): Node => {
    const { data, type } = determineNodeTypes(responseNode.codeBlock.code[0], responseNode.comments)
    return {
        id: responseNode.id,
        position: { x: 0, y: 0 }, // dagre will determine pos
        data,
        type,
    }
}

const determineEdgeHandles = (edge: Edge, nodeList: Node[]) => {
    const source = nodeList.find((n) => n.id === edge.source)
    const target = nodeList.find((n) => n.id === edge.target)
    if (!source || !target) return
    if (source.data.label === LOOP_BODY && target.data.label === LOOP_CONDITION) {
        edge.sourceHandle = 'c'
        edge.targetHandle = 'c'
    } else if (source.data.label === LOOP_CONDITION && target.data.label === LOOP_BODY) {
        edge.sourceHandle = 'b'
        edge.targetHandle = 'a'
    } else if (source.data.label === LOOP_CONDITION && target.data.label === AFTER_LOOP) {
        edge.sourceHandle = 'b'
        edge.targetHandle = 'a'
    } else if ((source.data.label === TRUE_BRANCH || source.data.label === FALSE_BRANCH) && target.data.label !== AFTER_IF_ELSE) {
        edge.sourceHandle = 'c'
        edge.targetHandle = 'c'
    }
}

const determineNodeTypes = (code: string, comments: string[]) => {
    let type = NodeType.BASIC_NODE
    let finalCode = code
    let nodeData
    if (code === ENTRY) {
        nodeData = entryNode
    } else if (code === EXIT) {
        nodeData = exitNode
    } else if (code === TRUE_BRANCH) {
        type = NodeType.BRANCH_NODE
        nodeData = trueBranchNode
    } else if (code === FALSE_BRANCH) {
        type = NodeType.BRANCH_NODE
        nodeData = falseBranchNode
    } else if (code === AFTER_IF_ELSE) {
        nodeData = afterIfElseNode
    } else if (code === LOOP_BODY) {
        nodeData = loopBodyNode
    } else if (code === AFTER_LOOP) {
        nodeData = afterLoopNode
    } else if (code.startsWith(IF_CONDITION)) {
        type = NodeType.CONDITION_NODE
        nodeData = ifConditionNode
        finalCode = code.substring(IF_CONDITION.length)
    } else if (code.startsWith(LOOP_CONDITION)) {
        type = NodeType.CONDITION_NODE
        nodeData = loopConditionNode
        finalCode = code.substring(LOOP_CONDITION.length)
    } else if (code.startsWith(FOR_LOOP) || code.startsWith(WHILE_LOOP)) {
        type = NodeType.COLLAPSIBLE_NODE
        nodeData = loopNode
        nodeData.label = code.startsWith(FOR_LOOP) ? FOR_LOOP : WHILE_LOOP
    } else if (code.startsWith(IF_ELSE)) {
        type = NodeType.COLLAPSIBLE_NODE
        nodeData = ifElseNode
        finalCode = code.substring(IF_ELSE.length)
    } else {
        type = NodeType.STATEMENT_NODE
        nodeData = statementNode
        finalCode = code.startsWith(STATEMENT) ? code.substring(STATEMENT.length) : code
    }
    return {
        data: { code: finalCode, comments, ...nodeData },
        type,
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
                determineEdgeHandles(edge, newNodeList)
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
