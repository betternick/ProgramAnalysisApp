import React from 'react'
import { Flex, Heading } from '@chakra-ui/react'
import Uploader from './Uploader'
import Executor from './Executor'
import { ResponseGraph } from '../types/ControlFlowGraphTypes'

type SidebarProps = {
    handleUpload: (response: JSON) => void
    handleExecute: (response: JSON) => void
    graph: ResponseGraph[]
}
export default function Sidebar({ handleUpload, handleExecute, graph }: SidebarProps) {
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
        </Flex>
    )
}
