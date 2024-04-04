import { Card, CardBody, Heading, Code } from '@chakra-ui/react'
import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { HandleInfo } from '../../types/MiscTypes'

export type ConditionNodeProps = {
    background: string
    label: string
    code: string
    handles: HandleInfo[]
    comments: string[]
}

export default function ConditionNode({ data }: NodeProps<ConditionNodeProps>) {
    const { label, code, background, handles, comments } = data
    return (
        <Card
            variant="solid"
            background={background}
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
