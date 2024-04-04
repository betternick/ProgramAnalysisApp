import { Flex, Heading, Tag } from '@chakra-ui/react'
import React from 'react'

export default function Comments2({ comments }) {
    const [UNREACHABLE, UNDECLARED] = ['Unreachable code', 'Undeclared variable']
    const tags = comments.map((c) => (
        <Tag
            colorScheme={c === UNREACHABLE ? 'red' : 'blue'}
            m={1}
        >
            {c}
        </Tag>
    ))
    return (
        <Flex
            flexDir="column"
            pt={1}
        >
            <Heading
                as="h6"
                size="xs"
            >
                Issues:
            </Heading>
            <Flex>{tags}</Flex>
        </Flex>
    )
}
