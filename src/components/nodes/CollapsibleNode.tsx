import { Handle, NodeProps } from 'reactflow'
import { HandleInfo } from '../../types/MiscTypes'
import { Card, CardBody, Heading, Code } from '@chakra-ui/react'
import React from 'react'

export type CollapsibleNodeProps = {
    borderColor: string
    label: string
    code: string
    handles: HandleInfo[]
    comments: string[]
}

export default function CollapsibleNode({ data }: NodeProps<CollapsibleNodeProps>) {
    const { borderColor, label, code, handles, comments } = data
    const codeLines = code.split('\n')
    return (
        <Card
            variant="outline"
            borderColor={borderColor}
        >
            <CardBody>
                <Heading size="sm">{label}</Heading>
                {codeLines.map((line, key) => (
                    <Code
                        display="block"
                        variant="subtle"
                        whiteSpace="pre"
                        children={line}
                        key={key}
                    />
                ))}
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
