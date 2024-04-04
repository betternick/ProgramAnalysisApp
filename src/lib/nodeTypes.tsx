import EntryNode from '../components/nodes/EntryNode'
import ExitNode from '../components/nodes/ExitNode'
import IfElseNode from '../components/nodes/IfElseNode'
import IfConditionNode from '../components/nodes/IfConditionNode'
import StatementNode from '../components/nodes/StatementNode'
import FalseBranchNode from '../components/nodes/FalseBranchNode'
import TrueBranchNode from '../components/nodes/TrueBranchNode'
import AfterIfElseNode from '../components/nodes/AfterIfElseNode'
import LoopNode from '../components/nodes/LoopNode'
import LoopConditonNode from '../components/nodes/LoopConditionNode'
import LoopBodyNode from '../components/nodes/LoopBodyNode'
import AfterLoopNode from '../components/nodes/AfterLoopNode'

export enum NodeType {
    ENTRY_NODE = 'entryNode',
    EXIT_NODE = 'exitNode',
    STATEMENT_NODE = 'statementNode',
    IF_ELSE_NODE = 'ifElseNode',
    IF_CONDITION_NODE = 'ifConditionNode',
    FALSE_BRANCH_NODE = 'falseBranchNode',
    TRUE_BRANCH_NODE = 'trueBranchNode',
    AFTER_IF_ELSE_NODE = 'afterIfElseNode',
    LOOP_NODE = 'loopNode',
    LOOP_CONDITION_NODE = 'loopConditionNode',
    LOOP_BODY_NODE = 'loopBodyNode',
    AFTER_LOOP_NODE = 'afterLoopNode',
}

export const nodeTypes = {
    entryNode: EntryNode,
    exitNode: ExitNode,
    statementNode: StatementNode,
    ifElseNode: IfElseNode,
    ifConditionNode: IfConditionNode,
    falseBranchNode: FalseBranchNode,
    trueBranchNode: TrueBranchNode,
    afterIfElseNode: AfterIfElseNode,
    loopNode: LoopNode,
    loopConditionNode: LoopConditonNode,
    loopBodyNode: LoopBodyNode,
    afterLoopNode: AfterLoopNode,
}

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
