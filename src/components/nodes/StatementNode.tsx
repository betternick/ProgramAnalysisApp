import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { Card, CardBody, Code, Heading } from '@chakra-ui/react'
import { CustomNodeWithCodeProps } from '../../types/MiscTypes'
import Comments from '../Comments'
import ExecutionData from '../ExecutionData'

export default function StatementNode({ data }: NodeProps<CustomNodeWithCodeProps>) {
    const { label, code, handles, comments, dynamicData } = data

    return (
        <Card
            variant="outline"
            borderColor="darkgray"
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
                {dynamicData && <ExecutionData data={dynamicData} />}
                {/* <ExecutionData data={dynamicData} /> */}
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
