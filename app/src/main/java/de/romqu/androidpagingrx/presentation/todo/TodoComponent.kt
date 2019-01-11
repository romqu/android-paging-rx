package de.romqu.androidpagingrx.presentation.todo

import dagger.Subcomponent
import de.romqu.androidpagingrx.presentation.todo.display.TodoDisplayFragment
import de.romqu.androidpagingrx.presentation.todo.display.TodoDisplayModule

@TodoScope
@Subcomponent(
    modules = [
        TodoDisplayModule::class
    ]
)
interface TodoComponent {

    fun inject(todoDisplayFragment: TodoDisplayFragment)

    @Subcomponent.Builder
    interface Builder {
        fun todoDisplayModule(module: TodoDisplayModule): Builder

        fun build(): TodoComponent
    }

}