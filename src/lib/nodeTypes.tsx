import StatementNode from '../components/nodes/StatementNode'
import BasicNode from '../components/nodes/BasicNode'
import { Position } from 'reactflow'
import ConditionNode from '../components/nodes/ConditionNode'
import CollapsibleNode from '../components/nodes/CollapsibleNode'
import BranchNode from '../components/nodes/BranchNode'

export const ENTRY = 'Entry'
export const EXIT = 'Exit'
export const STATEMENT = 'Statement: '
export const IF_ELSE = 'If-Else statement: '
export const IF_CONDITION = 'If condition: '
export const TRUE_BRANCH = 'True branch'
export const FALSE_BRANCH = 'False branch'
export const AFTER_IF_ELSE = 'After If-Else'
export const FOR_LOOP = 'For loop: '
export const WHILE_LOOP = 'While loop: '
export const LOOP_CONDITION = 'Loop condition: '
export const LOOP_BODY = 'Loop body'
export const AFTER_LOOP = 'After loop'

export const entryNode = {
    background: '#264653',
    label: ENTRY,
    handles: [{ type: 'source', position: Position.Bottom, id: 'a' }],
}

export const exitNode = {
    background: '#9b2226',
    label: EXIT,
    handles: [{ type: 'target', position: Position.Top, id: 'a' }],
}

export const trueBranchNode = {
    background: '#00bf7d',
    label: TRUE_BRANCH,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
        { type: 'source', position: Position.Left, id: 'c' },
    ],
}

export const falseBranchNode = {
    background: '#bf0603',
    label: FALSE_BRANCH,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
        { type: 'source', position: Position.Right, id: 'c' },
    ],
}

export const afterIfElseNode = {
    background: '#2a9d8f',
    label: AFTER_IF_ELSE,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
    ],
}

export const loopBodyNode = {
    background: '#bb3e03',
    label: LOOP_BODY,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
        { type: 'source', position: Position.Left, id: 'c' },
    ],
}

export const afterLoopNode = {
    background: '#e76f51',
    label: AFTER_LOOP,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
    ],
}

export const ifConditionNode = {
    background: '#2a9d8f',
    label: IF_CONDITION,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
    ],
}

export const loopConditionNode = {
    background: '#e76f51',
    label: LOOP_CONDITION,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
        { type: 'target', position: Position.Left, id: 'c' },
    ],
}

export const loopNode = {
    borderColor: '#f4a261',
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
    ],
}

export const ifElseNode = {
    borderColor: '#2a9d8f',
    label: IF_ELSE,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
    ],
}

export const statementNode = {
    borderColor: '#264653',
    label: STATEMENT,
    handles: [
        { type: 'target', position: Position.Top, id: 'a' },
        { type: 'source', position: Position.Bottom, id: 'b' },
    ],
}

export enum NodeType {
    STATEMENT_NODE = 'statementNode',
    BASIC_NODE = 'basicNode',
    CONDITION_NODE = 'conditionNode',
    COLLAPSIBLE_NODE = 'collapsibleNode',
    BRANCH_NODE = 'branchNode',
}

export const nodeTypes = {
    statementNode: StatementNode,
    basicNode: BasicNode,
    conditionNode: ConditionNode,
    collapsibleNode: CollapsibleNode,
    branchNode: BranchNode,
}
