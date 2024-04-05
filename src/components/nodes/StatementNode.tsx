import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { Card, CardBody, Code, Heading } from '@chakra-ui/react'
import { CustomNodeWithCodeProps } from '../../types/MiscTypes'
import Comments from '../Comments'

export default function StatementNode({ data }: NodeProps<CustomNodeWithCodeProps>) {
    const { color, label, code, handles, comments } = data

    return (
        <Card
            variant="outline"
            borderColor={color}
        >
            <CardBody>
                <Heading size="sm">{label}</Heading>
                <Code
                    display="block"
                    variant="subtle"
                    whiteSpace="pre"
                    children={code}
                />
                {comments.length > 0 && <Comments comments={comments} />}
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
