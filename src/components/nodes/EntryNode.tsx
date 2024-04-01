import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardHeader, Heading } from '@chakra-ui/react'

export default function EntryNode({ data }) {
    return (
        <Card
            background="#264653"
            color="white"
            variant="filled"
        >
            <CardHeader>
                <Heading size="sm">{data.label}</Heading>
            </CardHeader>
            <Handle
                type="source"
                position={Position.Bottom}
                id="a"
            />
        </Card>
    )
}
