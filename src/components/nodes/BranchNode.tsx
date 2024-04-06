import { Card, CardHeader, Heading } from '@chakra-ui/react'
import React from 'react'
import { NodeProps, Handle } from 'reactflow'
import { CustomNodeProps } from '../../types/MiscTypes'

export default function BranchNode({ data }: NodeProps<CustomNodeProps>) {
    const { color, label, handles } = data
    return (
        <Card
            background={color}
            color="white"
            variant="filled"
        >
            <CardHeader>
                <Heading size="sm">{label}</Heading>
            </CardHeader>
            {handles.map((h, key) => (
                <Handle
                    type={h.type}
                    position={h.position}
                    id={h.id}
                    key={key}
                />
            ))}
        </Card>
    )
}
