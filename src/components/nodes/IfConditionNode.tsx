import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardBody, Code, Heading } from '@chakra-ui/react'
import { IF_CONDITION } from '../../lib/nodeTypes'

export default function IfConditionNode({ data }) {
    const code = data.label.substring(IF_CONDITION.length)

    return (
        <Card
            variant="solid"
            background="#2a9d8f"
            color="white"
        >
            <Handle
                type="target"
                position={Position.Top}
                id="a"
            />
            <CardBody>
                <Heading size="sm">If Condition:</Heading>
                <Code
                    display="block"
                    variant="solid"
                    whiteSpace="pre"
                    colorScheme="whiteAlpha"
                    children={code}
                />
            </CardBody>
            <Handle
                type="source"
                position={Position.Bottom}
                id="b"
            />
        </Card>
    )
}
