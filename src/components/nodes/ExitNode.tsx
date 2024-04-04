import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardHeader, Heading } from '@chakra-ui/react'

export default function ExitNode({ data }) {
    return (
        <Card
            background="#9b2226"
            color="white"
            variant="filled"
        >
            <Handle
                type="target"
                position={Position.Top}
                id="a"
            />
            <CardHeader>
                <Heading size="sm">{data.label}</Heading>
            </CardHeader>
        </Card>
    )
}
