import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { Card, CardHeader, Heading } from '@chakra-ui/react'
import { CustomNodeProps } from '../../types/MiscTypes'

export default function BasicNode({ data }: NodeProps<CustomNodeProps>) {
    const { color, label, handles } = data
    return (
        <Card
            background={color}
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
