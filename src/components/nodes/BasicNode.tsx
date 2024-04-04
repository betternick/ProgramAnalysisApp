import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { Card, CardHeader, Heading } from '@chakra-ui/react'
import { HandleInfo } from '../../types/MiscTypes'

export type BasicNodeProps = {
    background: string
    label: string
    handles: HandleInfo[]
    comments: string[]
}

export default function BasicNode({ data }: NodeProps<BasicNodeProps>) {
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
