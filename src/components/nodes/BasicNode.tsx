import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { Card, CardBody, Heading, Text } from '@chakra-ui/react'
import { CustomNodeProps } from '../../types/MiscTypes'

export default function BasicNode({ data }: NodeProps<CustomNodeProps>) {
    const { color, label, handles } = data
    const lines = label.split('\n')
    return (
        <Card
            background={color}
            color="white"
            variant="filled"
        >
            <CardBody textAlign="center">
                <Heading size="sm">{lines[0]}</Heading>
                {lines.length > 1 && <Text>{lines[1]}</Text>}
            </CardBody>
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
