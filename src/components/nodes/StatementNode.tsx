import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { Card, CardBody, Code, Flex, Heading } from '@chakra-ui/react'
import { HandleInfo } from '../../types/MiscTypes'
import Comments from '../Comments'
import Comments2 from '../Comments2'

export type StatementNodeProps = {
    borderColor: string
    label: string
    code: string
    handles: HandleInfo[]
    comments: string[]
}

export default function StatementNode({ data }: NodeProps<StatementNodeProps>) {
    const { borderColor, label, code, handles, comments } = data

    return (
        <Card
            variant="outline"
            borderColor={borderColor}
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
