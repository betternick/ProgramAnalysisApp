import { Card, CardBody, Heading, Code } from '@chakra-ui/react'
import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { CustomNodeWithCodeProps } from '../../types/MiscTypes'

export default function ConditionNode({ data }: NodeProps<CustomNodeWithCodeProps>) {
    const { label, code, color, handles } = data
    return (
        <Card
            variant="solid"
            background={color}
            color="white"
        >
            <CardBody>
                <Heading size="sm">{label}</Heading>
                <Code
                    display="block"
                    variant="solid"
                    whiteSpace="pre"
                    colorScheme="whiteAlpha"
                    children={code}
                />
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
