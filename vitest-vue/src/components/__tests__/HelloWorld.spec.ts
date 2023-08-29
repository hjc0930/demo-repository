import { describe, test, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import HelloWorld from '../HelloWorld.vue'

describe('HelloWorld', () => {
  test('renders properly', () => {
    const wrapper = mount(HelloWorld, { props: { msg: 'Hello Vitest' } })
    expect(wrapper.text()).toContain('Hello Vitest')
  })

  test("primary properly", () => {
    const wrapper = mount(HelloWorld, { props: { type: "primary" } });
    expect(wrapper.classes()).toContain("greetings")
  })
})
