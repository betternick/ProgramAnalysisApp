import { Handle, NodeProps } from 'reactflow'
import { CustomNodeWithCodeProps } from '../../types/MiscTypes'
import { Card, CardBody, Heading, Code } from '@chakra-ui/react'
import React from 'react'
import ExecutionData from '../ExecutionData'

export default function CollapsibleNode({ data }: NodeProps<CustomNodeWithCodeProps>) {
    const { color, label, code, handles, dynamicData } = data
    const codeLines = code.split('\n')
    return (
        <Card
            variant="outline"
            borderColor={color}
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
                {dynamicData && <ExecutionData data={dynamicData} />}
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
