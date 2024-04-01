import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardHeader, Heading } from '@chakra-ui/react'

export default function LoopBodyNode({ data }) {
    return (
        <Card
            background="#bb3e03"
            color="white"
            variant="filled"
        >
            <Handle
                type="target"
                position={Position.Top}
                id="a"
            />
            <CardHeader>
                <Heading size="sm">Loop Body</Heading>
            </CardHeader>
            <Handle
                type="source"
                position={Position.Bottom}
                id="b"
            />
            <Handle
                type="source"
                position={Position.Left}
                id="c"
            />
        </Card>
    )
}
