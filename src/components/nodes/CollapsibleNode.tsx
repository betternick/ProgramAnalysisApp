import { Card, CardBody, Heading, Text } from '@chakra-ui/react'
import React from 'react'
import { NodeProps, Handle } from 'reactflow'
import { CustomNodeCollapsibleProps } from '../../types/MiscTypes'

export default function CollapsibleNode({ data }: NodeProps<CustomNodeCollapsibleProps>) {
    const { color, label, handles } = data
    return (
        <Card
            background={color}
            color="white"
            variant="filled"
        >
            <CardBody textAlign="center">
                <Heading size="sm">{label}</Heading>
                <Text fontSize="xs">(Click to show/hide)</Text>
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
