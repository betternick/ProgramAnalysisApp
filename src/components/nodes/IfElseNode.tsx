import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardBody, Code, Heading } from '@chakra-ui/react'
import { IF_ELSE } from '../../lib/nodeTypes'

export default function IfElseNode({ data }) {
    const code = data.label.substring(IF_ELSE.length)
    const codeLines = code.split('\n')

    return (
        <Card
            variant="outline"
            borderColor="#2a9d8f"
        >
            <Handle
                type="target"
                position={Position.Top}
                id="a"
            />
            <CardBody>
                <Heading size="sm">If-Else Statement:</Heading>
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
