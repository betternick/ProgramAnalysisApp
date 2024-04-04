import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardHeader, Heading } from '@chakra-ui/react'

export default function AfterLoopNode({ data }) {
    return (
        <Card
            background="#e76f51"
            color="white"
            variant="filled"
        >
            <Handle
                type="target"
                position={Position.Top}
                id="a"
            />
            <CardHeader>
                <Heading size="sm">After Loop</Heading>
            </CardHeader>
            <Handle
                type="source"
                position={Position.Bottom}
                id="b"
            />
        </Card>
    )
}
