import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardBody, Code, Heading } from '@chakra-ui/react'
import { FOR_LOOP, WHILE_LOOP } from '../../lib/nodeTypes'

export default function LoopNode({ data }) {
    const loopLabel = data.label.startsWith(FOR_LOOP) ? FOR_LOOP : WHILE_LOOP
    const code = data.label.substring(loopLabel.length)
    const codeLines = code.split('\n')

    return (
        <Card
            variant="outline"
            borderColor="#f4a261"
        >
            <Handle
                type="target"
                position={Position.Top}
                id="a"
            />
            <CardBody>
                <Heading size="sm">{loopLabel}</Heading>
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
            <Handle
                type="source"
                position={Position.Bottom}
                id="b"
            />
        </Card>
    )
}
