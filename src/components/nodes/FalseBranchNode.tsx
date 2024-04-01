import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardHeader, Heading } from '@chakra-ui/react'

export default function FalseBranchNode({ data }) {
    return (
        <Card
            background="#bf0603"
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
            <Handle
                type="source"
                position={Position.Bottom}
                id="b"
            />
        </Card>
    )
}
