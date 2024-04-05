import { useDisclosure, Alert, AlertIcon, AlertTitle, Flex, ListItem, IconButton, UnorderedList } from '@chakra-ui/react'
import React from 'react'
import { IoChevronDown, IoChevronUp } from 'react-icons/io5'

export default function Comments({ comments }) {
    const { isOpen: isVisible, onClose, onOpen } = useDisclosure({ defaultIsOpen: false })

    return (
        <Flex
            width="100%"
            mt={1}
        >
            <Alert status="error">
                <Flex>
                    <AlertIcon />
                    <div>
                        <Flex>
                            <AlertTitle>This block contains problematic code</AlertTitle>
                            {isVisible ? (
                                <IconButton
                                    icon={<IoChevronUp />}
                                    size="xs"
                                    aria-label="Close Alert"
                                    onClick={onClose}
                                    variant="unstyled"
                                    display="inline-flex"
                                />
                            ) : (
                                <IconButton
                                    icon={<IoChevronDown />}
                                    size="xs"
                                    aria-label="Open Alert"
                                    variant="unstyled"
                                    onClick={onOpen}
                                    display="inline-flex"
                                />
                            )}
                        </Flex>
                        {isVisible && (
                            <UnorderedList flexWrap="wrap">
                                {comments.map((c: string, key: number) => (
                                    <ListItem
                                        key={key}
                                        wordBreak="break-all"
                                    >
                                        {c}
                                    </ListItem>
                                ))}
                            </UnorderedList>
                        )}
                    </div>
                </Flex>
            </Alert>
        </Flex>
    )
}
