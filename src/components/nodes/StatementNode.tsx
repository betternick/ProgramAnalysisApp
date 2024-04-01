import React from 'react'
import { Handle, Position } from 'reactflow'
import { Card, CardBody, Code, Heading } from '@chakra-ui/react'
import { STATEMENT } from '../../lib/nodeTypes'

export default function StatementNode({ data }) {
    const code = data.label.substring(STATEMENT.length)

    return (
        <Card
            variant="outline"
            borderColor="#264653"
        >
            <Handle
                type="target"
                position={Position.Top}
                id="a"
            />
            <CardBody>
                <Heading size="sm">Statement:</Heading>
                <Code
                    display="block"
                    variant="subtle"
                    whiteSpace="pre"
                    children={code}
                />
            </CardBody>
            <Handle
                type="source"
                position={Position.Bottom}
                id="b"
            />
        </Card>
    )
}
