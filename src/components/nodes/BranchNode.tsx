import { Card, CardHeader, Heading } from '@chakra-ui/react'
import React from 'react'
import { NodeProps, Handle } from 'reactflow'
import { BasicNodeProps } from './BasicNode'

export default function BranchNode({ data }: NodeProps<BasicNodeProps>) {
    const { background, label, handles, comments } = data
    return (
        <Card
            background={background}
            color="white"
            variant="filled"
        >
            {handles.map((h, key) => (
                <Handle
                    type={h.type}
                    position={h.position}
                    id={h.id}
                    key={key}
                />
            ))}
            <CardHeader>
                <Heading size="sm">{label}</Heading>
            </CardHeader>
        </Card>
    )
}
