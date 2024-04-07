import React from 'react'
import { Flex, Heading } from '@chakra-ui/react'
import Uploader from './Uploader'
import Executor from './Executor'
import { ResponseGraph } from '../types/ControlFlowGraphTypes'
import StatPicker from './StatPicker'

type SidebarProps = {
    handleUpload: (response: JSON) => void
    handleExecute: (response: JSON) => void
    handleStatChange: (stat: string) => void
    graph: ResponseGraph[]
}
export default function Sidebar({ handleUpload, handleExecute, graph, handleStatChange }: SidebarProps) {
    return (
        <Flex
            direction="column"
            gap={2}
            m={3}
        >
            <Heading
                textAlign="center"
                size="lg"
            >
                Program Analyser
            </Heading>
            <Uploader handleResponse={handleUpload} />
            {graph.length > 0 && <Executor handleResponse={handleExecute} />}
            {graph.length > 0 && graph[0].nodes[0].dynamicData && <StatPicker handleStatChange={handleStatChange} />}
        </Flex>
    )
}
