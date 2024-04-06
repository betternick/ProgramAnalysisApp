import { Card, CardBody, Heading, Code } from '@chakra-ui/react'
import React from 'react'
import { Handle, NodeProps } from 'reactflow'
import { CustomNodeWithCodeProps } from '../../types/MiscTypes'
import ExecutionData from '../ExecutionData'

export default function ConditionNode({ data }: NodeProps<CustomNodeWithCodeProps>) {
    const { label, code, color, handles, dynamicData } = data
    return (
        <Card
            variant="outline"
            background={color}
        >
            <CardBody color="white">
                <Heading size="sm">{label}</Heading>
                <Code
                    display="block"
                    variant="solid"
                    whiteSpace="pre"
                    colorScheme="whiteAlpha"
                    children={code}
                />
            </CardBody>
            {dynamicData && (
                <CardBody background="white">
                    <ExecutionData data={dynamicData} />
                </CardBody>
            )}
            {/* <CardBody background="white">
                <ExecutionData data={dynamicData} />
            </CardBody> */}
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
