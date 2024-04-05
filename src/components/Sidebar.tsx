import React from 'react'
import { Flex, Heading } from '@chakra-ui/react'
import Uploader from './Uploader'
import Executor from './Executor'

type SidebarProps = {
    handleResponse: (response: JSON) => void
}
export default function Sidebar({ handleResponse }: SidebarProps) {
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
                Control Flow Graph Generator
            </Heading>
            <Uploader handleResponse={handleResponse} />
            <Executor />
        </Flex>
    )
}
