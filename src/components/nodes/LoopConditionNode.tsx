import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardBody, Code, Heading } from '@chakra-ui/react'
import { LOOP_CONDITION } from '../../lib/nodeTypes'

export default function LoopConditonNode({ data }) {
    const code = data.label.substring(LOOP_CONDITION.length)

    return (
        <Card
            variant="solid"
            background="#e76f51"
            color="white"
        >
            <Handle
                type="target"
                position={Position.Top}
                id="a"
            />
            <CardBody>
                <Heading size="sm">Loop Condition:</Heading>
                <Code
                    display="block"
                    variant="solid"
                    whiteSpace="pre"
                    colorScheme="whiteAlpha"
                    children={code}
                />
            </CardBody>
            <Handle
                type="target"
                position={Position.Left}
                id="c"
            />
            <Handle
                type="source"
                position={Position.Bottom}
                id="b"
            />
        </Card>
    )
}
